import http from 'k6/http';
import { check, group } from "k6";

export let options = {
    stages: [
        { duration: "120s", target: 50 }
    ]
};

const failingSpecResultsXml = open('../test-fixtures/src/main/resources/TEST-projektor.example.spock.FailingSpec.xml');
const passingSpecResultsXml = open('../test-fixtures/src/main/resources/TEST-projektor.example.spock.PassingSpec.xml');

const resultsPayload = JSON.stringify({
    groupedTestSuites: [
        {
            groupName: "group1",
            testSuitesBlob: failingSpecResultsXml
        },
        {
            groupName: "group2",
            testSuitesBlob: passingSpecResultsXml
        }
    ]
});

const resultsParams = {headers: {"Content-Type": "application/json"}};

export function setup() {
    const resultsResponse = http.post(
        `http://localhost:8080/groupedResults/`,
        resultsPayload,
        resultsParams
    );

    const testId = JSON.parse(resultsResponse.body).id

    console.log("Test ID: " + testId)

    return { testId: testId }
}

export default function (data) {
    const testId = data.testId;

    const statusCheck200 = {
        "status is 200": (r) => r.status === 200
    }

    group('fetch test run details', () => {
        const testRunResponse = http.get(`http://localhost:8080/run/${testId}`);
        check(testRunResponse, statusCheck200);

        const testRunSummaryResponse = http.get(`http://localhost:8080/run/${testId}/summary`);
        check(testRunSummaryResponse, statusCheck200);
    })
};