var reporter = require('cucumber-html-reporter');

var options = {
    theme: 'bootstrap',
    jsonFile: 'e2e-reports/results.json',
    output: 'e2e-reports/cucumber_report.html',
    reportSuiteAsScenarios: true
};

reporter.generate(options);
