export const sourceHandler = (source) => {
  const reactReg = /import React, {[^}]*}.*(?='react').*/gim;
  const patternflyReg = /import {[^}]*}.*(?='@patternfly\/react-core').*/gim;
  const regexvalueReact = new RegExp(reactReg);
  const reactImport = regexvalueReact.exec(source);
  const reg = /\{([^)]+)\}/;
  const reactElements = reg.exec(reactImport[0])[1];
  const regexvaluePat = new RegExp(patternflyReg);
  const patternflyImport = regexvaluePat.exec(source);
  const patternflyElements = reg.exec(patternflyImport[0])[1];
  const trimmedSource = source
    .split(reactReg)
    .join('')
    .trim()
    .split(patternflyReg)
    .join('')
    .trim();

  const formName = trimmedSource.split(':')[0].split('const ')[1];

  return { reactElements, patternflyElements, formName, trimmedSource };
};
