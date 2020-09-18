/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import FeaturesScoreTable from '../FeaturesScoreTable';
import { mount, shallow } from 'enzyme';

describe('FeaturesScoreTable', () => {
  test('renders correctly a score table', () => {
    const wrapper = shallow(<FeaturesScoreTable featuresScore={scores} />);

    expect(wrapper).toMatchSnapshot();
  });
  test('renders two lists of positive and negative outcomes', () => {
    const wrapper = mount(<FeaturesScoreTable featuresScore={scores} />);

    expect(wrapper.find('DataList')).toHaveLength(2);
    expect(
      wrapper
        .find('DataList')
        .at(0)
        .find('DataListItem')
    ).toHaveLength(4);
    expect(
      wrapper
        .find('DataList')
        .at(1)
        .find('DataListItem')
    ).toHaveLength(3);
  });
});

const scores = [
  {
    featureName: 'Liabilities',
    featureScore: 0.6780527129423648
  },
  {
    featureName: 'Lender Ratings',
    featureScore: -0.08937896629080377
  },
  {
    featureName: 'Employment Income',
    featureScore: -0.9240811677386516
  },
  {
    featureName: 'Liabilities 2',
    featureScore: 0.7693802543201365
  },
  {
    featureName: 'Assets',
    featureScore: 0.21272743757961554
  }
];
