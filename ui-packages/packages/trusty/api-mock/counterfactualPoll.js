const faker = require('faker');

let hit = 0;
let executionId = null;
let cfResults = [];

module.exports = (req, res, next) => {
  const _send = res.send;
  res.send = function(body) {
    if (req.path === '/counterfactuals') {
      const query = req.query;
      if (req.method === 'POST') {
        try {
          return _send.call(
            this,
            JSON.stringify({
              executionId: query.executionId,
              counterfactualId: faker.random.uuid()
            })
          );
        } catch (e) {}
      }
      if (req.method === 'GET') {
        if (executionId === null || executionId !== query.executionId) {
          executionId = query.executionId;
          hit = 0;
          cfResults = [];
        }
        hit++;
        if (hit === 2) {
          cfResults.push(getResult(executionId, cfResults.length, false));
          cfResults.push(getResult(executionId, cfResults.length, false));
          cfResults.push(getResult(executionId, cfResults.length, false));
        }
        if (hit === 3) {
          cfResults.push(getResult(executionId, cfResults.length, false));
          cfResults.push(getResult(executionId, cfResults.length, true));
        }
        try {
          const json = JSON.parse(body);
          return _send.call(
            this,
            JSON.stringify({
              ...json,
              executionId,
              counterfactualId: query.counterfactualId,
              solutions: cfResults
            })
          );
        } catch (e) {}
      }
    }
    return _send.call(this, body);
  };
  next();
};

function getResult(executionId, solutionIdBase, isFinal) {
  return {
    ...interim,
    executionId,
    solutionId: (solutionIdBase + 10001).toString(),
    stage: isFinal ? 'FINAL' : 'INTERMEDIATE'
  };
}

const interim = {
  type: 'counterfactual',
  valid: true,
  executionId: 'executionId',
  status: 'SUCCEEDED',
  statusDetails: '',
  counterfactualId: 'counterfactualId',
  solutionId: 'solution1',
  isValid: true,
  stage: 'INTERMEDIATE',
  inputs: [],
  outputs: []
};
