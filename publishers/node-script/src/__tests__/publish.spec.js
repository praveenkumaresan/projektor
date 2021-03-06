const waitForExpect = require("wait-for-expect");
const axios = require("axios");
const MockAdapter = require("axios-mock-adapter");
const {
  collectResults,
  sendResults,
  collectAndSendResults,
} = require("../publish");

describe("Projektor publisher", () => {
  let mockAxios;

  beforeEach(() => {
    mockAxios = new MockAdapter(axios);
  });

  afterEach(() => {
    mockAxios.restore();
  });

  it("should gather results from one directory and send it to server", (done) => {
    const fileGlob = "src/__tests__/resultsDir1/*.xml";
    const serverUrl = "http://localhost:8080";

    mockAxios
      .onPost("http://localhost:8080/results")
      .reply(200, { id: "ABC123", uri: "/tests/ABC123" });

    const resultsBlob = collectResults([fileGlob]);

    sendResults(serverUrl, null, resultsBlob).then((respData) => {
      expect(respData.id).toBe("ABC123");
      expect(respData.uri).toBe("/tests/ABC123");

      expect(mockAxios.history.post.length).toBe(1);

      const postData = mockAxios.history.post[0].data;

      expect(postData).toContain("resultsDir1-results1");
      expect(postData).toContain("resultsDir1-results2");
      expect(postData).not.toContain("resultsDir2-results2");
      expect(postData).not.toContain("resultsDir2-results2");

      done();
    });
  });

  it("should gather results from glob that matches multiple directories", () => {
    const fileGlob = "src/__tests__/resultsDir*/*.xml";

    const resultsBlob = collectResults([fileGlob]);

    expect(resultsBlob).toContain("resultsDir1-results1");
    expect(resultsBlob).toContain("resultsDir1-results2");
    expect(resultsBlob).toContain("resultsDir1-results1");
    expect(resultsBlob).toContain("resultsDir2-results2");
  });

  it("should gather results from glob that matches multiple nested directories", () => {
    const fileGlob = "src/__tests__/nestedResultsDir/**/*";

    const resultsBlob = collectResults([fileGlob]);

    expect(resultsBlob).toContain("resultsDir1-results1");
    expect(resultsBlob).toContain("resultsDir1-results2");
    expect(resultsBlob).toContain("resultsDir1-results1");
    expect(resultsBlob).toContain("resultsDir2-results2");
  });

  it("should gather results from glob that matches single file", () => {
    const fileGlob = "src/__tests__/resultsDir2/results2.xml";

    const resultsBlob = collectResults([fileGlob]);

    expect(resultsBlob).not.toContain("resultsDir1-results1");
    expect(resultsBlob).not.toContain("resultsDir1-results2");
    expect(resultsBlob).not.toContain("resultsDir1-results1");
    expect(resultsBlob).toContain("resultsDir2-results2");
  });

  it("should collect and send results", async () => {
    const fileGlob = "src/__tests__/resultsDir1/*.xml";
    const serverUrl = "http://localhost:8080";

    mockAxios
      .onPost("http://localhost:8080/results")
      .reply(200, { id: "ABC123", uri: "/tests/ABC123" });

    collectAndSendResults(serverUrl, null, [fileGlob]);

    await waitForExpect(() => {
      expect(mockAxios.history.post.length).toBe(1);

      const postData = mockAxios.history.post[0].data;

      expect(postData).toContain("resultsDir1-results1");
      expect(postData).toContain("resultsDir1-results2");
    });
  });

  it("should include publish token in header when specified", async () => {
    const fileGlob = "src/__tests__/resultsDir1/*.xml";
    const serverUrl = "http://localhost:8080";

    mockAxios
      .onPost("http://localhost:8080/results")
      .reply(200, { id: "ABC123", uri: "/tests/ABC123" });

    const publishToken = "myPublishToken";

    collectAndSendResults(serverUrl, publishToken, [fileGlob]);

    await waitForExpect(() => {
      expect(mockAxios.history.post.length).toBe(1);

      const postRequest = mockAxios.history.post[0];
      expect(postRequest.headers["X-PROJEKTOR-TOKEN"]).toBe(publishToken);

      const postData = postRequest.data;
      expect(postData).toContain("resultsDir1-results1");
      expect(postData).toContain("resultsDir1-results2");
    });
  });

  it("should handle error when posting to server", async () => {
    const fileGlob = "src/__tests__/resultsDir1/*.xml";
    const serverUrl = "http://localhost:8080";

    mockAxios.onPost("http://localhost:8080/results").reply(400, null);

    collectAndSendResults(serverUrl, null, [fileGlob]);

    await waitForExpect(() => {
      expect(mockAxios.history.post.length).toBe(1);

      const postData = mockAxios.history.post[0].data;

      expect(postData).toContain("resultsDir1-results1");
      expect(postData).toContain("resultsDir1-results2");
    });
  });
});
