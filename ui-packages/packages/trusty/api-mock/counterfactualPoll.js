const faker = require('faker');
const inputData = require('./mocks/inputData');

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
          for (let i = 0; i < 5; i++) {
            cfResults.push(
              getResult(query.executionId, cfResults.length, false)
            );
          }
        }
        if (hit === 3) {
          for (let i = 0; i < 6; i++) {
            cfResults.push(
              getResult(query.executionId, cfResults.length, false)
            );
          }
          cfResults.push(getResult(query.executionId, cfResults.length, true));
          executionId = null;
        }
        try {
          const json = JSON.parse(body);
          return _send.call(
            this,
            JSON.stringify({
              ...json,
              executionId: query.executionId,
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
    stage: isFinal ? 'FINAL' : 'INTERMEDIATE',
    inputs: inputData.inputs.map((input, index) => {
      if (index === 0) {
        return {
          ...input,
          value: Math.floor(
            Math.random() * input.value * 0.2 * (Math.random() > 0.5 ? 1 : -1) +
              input.value
          )
        };
      }
      return input;
    })
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
