import React from 'react';
import { render } from '@testing-library/react';
import ProcessDiagram from '../ProcessDiagram';
jest.mock('react-svg-pan-zoom');
describe('ProcessDiagram component tests', () => {
  it('Snapshot testing  with default props', () => {
    const svg = {
      props: {
        src: '<svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="800" height="300" viewBox="0 0 1748 632"></svg>'
      }
    };
    const container = render(
      <ProcessDiagram svg={svg} width={100} height={100} />
    );
    expect(container).toMatchSnapshot();
  });
});
