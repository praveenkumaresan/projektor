import http from 'k6/http';
import {check} from "k6";

export let options = {
    stages: [
        {duration: "60s", target: 100}
    ]
};

const params = {headers: {"Content-Type": "application/json"}};

const passingSpecResultsXml = open('../test-fixtures/src/main/resources/TEST-projektor.example.spock.PassingSpec.xml');

export default function () {
    const response = http.post(
        `http://localhost:8080/results/`,
        passingSpecResultsXml,
        params
    );

    check(response, {
        "status is 200": (r) => r.status === 200
    });
};