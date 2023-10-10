import React from 'react';
import { PageTitle } from '../PageTitle';
import { Label } from '@patternfly/react-core/dist/js/components/Label';
import { render, screen } from '@testing-library/react';

describe('PageTitle test', () => {
  it('default snapshot testing', () => {
    const { container } = render(<PageTitle title="Title" />);

    expect(container).toMatchSnapshot();
  });

  it('snapshot testing with extra', () => {
    const { container } = render(
      <PageTitle title="Title" extra={<Label>Label</Label>} />
    );

    expect(container).toMatchSnapshot();

    const extra = screen.getAllByText('Label');

    expect(extra).toBeTruthy();
  });
});
