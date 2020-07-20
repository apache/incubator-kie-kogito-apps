import React from 'react';
import { shallow } from 'enzyme';
import FormattedDate from '../FormattedDate';
import { format } from 'date-fns';

describe('FormattedDate', () => {
  test('displays a formatted date', () => {
    const wrapper = shallow(<FormattedDate date="2020-01-01" />);
    expect(wrapper.find('span').text()).toMatch('Jan 1, 2020');
  });

  test('renders a tooltip with the complete date and time info', () => {
    const initialDate = '2020-01-01';
    const wrapper = shallow(<FormattedDate date={initialDate} />);
    const tooltip = wrapper.find('Tooltip');
    const fullDate = format(new Date(initialDate), 'PPpp');

    expect(wrapper.find('span').text()).toMatch('Jan 1, 2020');
    expect(tooltip.length).toBe(1);
    expect(tooltip.props().content).toMatch(fullDate);
  });

  test('displays the "on" preposition before the date when preposition prop is passed', () => {
    const wrapper = shallow(<FormattedDate date="2020-01-01" preposition />);
    expect(wrapper.find('span').text()).toMatch('on Jan 1, 2020');
  });

  test('displays full date and time when fullDateAndTime prop is passed', () => {
    const wrapper = shallow(
      <FormattedDate date="2020-01-01" fullDateAndTime />
    );
    expect(wrapper.find('Tooltip').length).toBe(0);
    expect(wrapper.find('span').text()).toMatch('Jan 1, 2020, 1:00:00 AM');
  });

  test('displays a relative timestamp if the date is in the last 24h', () => {
    const date = new Date().toISOString();
    const wrapper = shallow(<FormattedDate date={date} />);
    expect(wrapper.find('span').text()).toMatch('0 seconds ago');
  });
});
