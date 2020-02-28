package cukeFiles;


import cucumber.api.CucumberOptions;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import gherkin.deps.com.google.gson.JsonArray;
import helper.EmailUtil;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static helper.EmailUtil.getReportFooter;


//rerun txt
//combine rerun json file
//cucumber options

@CucumberOptions(
        plugin = { "pretty", "json:target/cucumber-reports/Cucumber.json",
                "junit:target/cucumber-reports/Cucumber.xml",
                "html:target/cucumber-reports","rerun:target/rerun/rerun.txt"},
        features = ".",
        glue= {"stepdefinitions"},
        tags = {"@amazon1"}

)

public class CukeRunner {

    private TestNGCucumberRunner testNGCucumberRunner;

    @BeforeSuite(alwaysRun = true)
    public void setUpClass() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = "cucumber", description = "Runs cucmber Features", dataProvider = "features")
    public void feature(CucumberFeatureWrapper cucumberFeature) {
        testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
    }

    @DataProvider
    public Object[][] features() {

        return testNGCucumberRunner.provideFeatures();
    }

    @Parameters("outputFiles")
    @AfterSuite(alwaysRun = true)
    public void testDownClass(String opFile) throws JSONException {
        generateReport(opFile);
    }

    @AfterClass
    public void streamClose(){
        testNGCucumberRunner.finish();
    }



    public void generateReport(String outputDirectory) throws JSONException {

        String[] reportCombine=outputDirectory.split(";");
        File reportOutputDirectory=new File("C:\\CucumberReports\\CucumberReports"+getCurrentTimeStamp());
//        File reportOutputDirectory=new File("target/MasterThoughtReports"+getCurrentTimeStamp());
        List<String> jsonFiles=new ArrayList<>();
        if(reportCombine.length>1) {
            combineResultJson(reportCombine[0], reportCombine[1]);
            jsonFiles.add(reportCombine[0]);
        }
        else{
            jsonFiles.add(reportCombine[0]);
        }

        String projectName="Project - Automation BDD";

        Configuration configuration=new Configuration(reportOutputDirectory,projectName);

        configuration.setRunWithJenkins(false);
        configuration.setParallelTesting(false);
        configuration.setBuildNumber("1");

        configuration.addClassifications("Platform","Windows");
        configuration.addClassifications("Browser","Chrome");
        configuration.addClassifications("Environment","QA");

        ReportBuilder reportBuilder=new ReportBuilder(jsonFiles,configuration);
        reportBuilder.generateReports();
        String reportOverview=getReportFooter();
//        reportOverview=reportOverview.replace("href=\"","href=\"file:"+reportOutputDirectory+"\\cucumber-html-reports\\");
     //   reportOverview=reportOverview.replace("href=","href=file:///"+reportOutputDirectory+"\\cucumber-html-reports\\");
        String filePath=reportOutputDirectory+"/cucumber-html-reports/overview-features.html";
        System.out.println(reportOverview);
        EmailUtil.sendEmail("amit.ranjan0703@gmail.com","UI Auto-Amazon Framework",reportOverview,filePath);

    }

    public void combineResultJson(String resultFile,String rerunResultFile) throws JSONException {
      System.out.print("combine results");
        JSONArray resultFeatures=new JSONArray(readFromFile(resultFile));
        JSONArray rerunFeatures=new JSONArray(readFromFile(rerunResultFile));
        for(int i=0;i<resultFeatures.length();i++){
            JSONObject resultFeature=resultFeatures.getJSONObject(i);
            String id=resultFeature.get("id").toString();
            for(int j=0;j<rerunFeatures.length();j++){
                if(id.equalsIgnoreCase(rerunFeatures.getJSONObject(j).get("id").toString())){
                    JSONArray featureResult=resultFeatures.getJSONObject(i).getJSONArray("elements");
                    JSONArray featureReRunResult=rerunFeatures.getJSONObject(j).getJSONArray("elements");
                    for(int itr=0;itr<featureResult.length();itr++){
                        JSONObject tcResult=featureResult.getJSONObject(itr);
                        String tcId=tcResult.get("id").toString();
                        for(int tcItr=0;tcItr<featureReRunResult.length();tcItr++){
                            if(tcId.equalsIgnoreCase(featureReRunResult.getJSONObject(tcItr).get("id").toString())){
                                featureResult.put(itr,featureReRunResult.getJSONObject(tcItr));
                            }
                        }
                    }
                    resultFeatures.getJSONObject(i).put("elements",featureResult);
                }
                resultFeatures.put(i,resultFeature);
            }
        }
        writeTotFile(resultFile,resultFeatures.toString().toCharArray());

    }

    public String readFromFile(String fileName){
        try{
            String line="";
            StringBuffer content =new StringBuffer();
            BufferedReader rd=new BufferedReader(new FileReader(fileName));
            while((line=rd.readLine())!=null){
                content.append(line);
            }
            return content.toString();
        }catch (Exception e){
            e.printStackTrace();
            return  "";
        }
    }
    public void writeTotFile(String fileName,char[] content){
        try{
            BufferedWriter wr=new BufferedWriter(new FileWriter(fileName));
            wr.write(content);
            wr.flush();
            wr.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getCurrentTimeStamp(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date now=new Date();
        String sdate=simpleDateFormat.format(now);
        return sdate;
    }
}
