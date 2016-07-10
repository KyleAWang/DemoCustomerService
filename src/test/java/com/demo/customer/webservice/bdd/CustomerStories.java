package com.demo.customer.webservice.bdd;

import com.demo.customer.webservice.bdd.steps.CustomerLifeSteps;
import com.demo.customer.webservice.bdd.steps.CustomerSteps;
import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.PendingStepStrategy;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;

/**
 * Created by Kyle
 */
public class CustomerStories extends JUnitStories {


    public CustomerStories() {
        configuredEmbedder().embedderControls().doGenerateViewAfterStories(true).doIgnoreFailureInStories(false)
                .doVerboseFailures(true);
    }

    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();

        Properties viewResources = new Properties();
        viewResources.put("decorateNonHtml", "true");

        PendingStepStrategy pendingStepStrategy = new FailingUponPendingStep();
        CrossReference crossReference = new CrossReference().withJsonOnly().withPendingStepStrategy(pendingStepStrategy)
                .withOutputAfterEachStory(true).excludingStoriesWithNoExecutedScenarios(true);

        StoryReporterBuilder reporterBuilder = new StoryReporterBuilder()
                .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass)).withFailureTrace(true)
                .withFailureTraceCompression(true).withDefaultFormats().withDefaultFormats()
                .withViewResources(viewResources)
                .withCrossReference(crossReference);

        // Start from default ParameterConverters instance
        ParameterConverters parameterConverters = new ParameterConverters();
        // factory to allow parameter conversion and loading from external resources (used by StoryParser too)
        ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(new LocalizedKeywords(), new LoadFromClasspath(embeddableClass), parameterConverters);
        // add custom converters
        parameterConverters.addConverters(new ParameterConverters.DateConverter(new SimpleDateFormat("yyyy-MM-dd")),
                new ParameterConverters.ExamplesTableConverter(examplesTableFactory));
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useStoryParser(new RegexStoryParser(examplesTableFactory))
                .useStoryReporterBuilder(reporterBuilder)
                .useParameterConverters(parameterConverters)
                // use '%' instead of '$' to identify parameters
                .useStepPatternParser(new RegexPrefixCapturingPatternParser(
                        "%"))
                .useStepMonitor(crossReference.getStepMonitor());

    }


    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new CustomerSteps(), new CustomerLifeSteps());
    }

    @Override
    protected List<String> storyPaths() {

        List<String> list = new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
                asList("**/stories/*.story"), null);
        return list;

    }


}
