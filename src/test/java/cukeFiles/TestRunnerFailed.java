package cukeFiles;


import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.testng.annotations.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//rerun txt
//combine rerun json file
//cucumber options

@CucumberOptions(
        features = "@target/rerun/rerun.txt",
        glue= {"stepdefinitions"},
        plugin = {
                "pretty", "html:target/cucumber-reports/single",
                "json:target/cucumber-reports/rerun_cucumber.json"}
)


 public class TestRunnerFailed{

    @Test
    public void runCukes(){
        TestNGCucumberRunner testNGCucumberRunner=new TestNGCucumberRunner(this.getClass());
        testNGCucumberRunner.runCukes();
    }



}