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
import React, { useEffect, useState } from 'react';
import {
  Nav,
  NavItem,
  NavList,
  PageSection,
  PageSectionVariants,
  Stack,
  StackItem
} from '@patternfly/react-core';
import {
  Link,
  Redirect,
  Route,
  Switch,
  useLocation,
  useParams,
  useRouteMatch
} from 'react-router-dom';
import { ExecutionRouteParams } from '../../../types';
import SkeletonFlexStripes from '../../Molecules/SkeletonFlexStripes/SkeletonFlexStripes';
import useExecutionInfo from './useExecutionInfo';
import ExecutionHeader from '../../Organisms/ExecutionHeader/ExecutionHeader';
import SkeletonStripe from '../../Atoms/SkeletonStripe/SkeletonStripe';
import SkeletonCards from '../../Molecules/SkeletonCards/SkeletonCards';
import ExecutionDetail from '../ExecutionDetail/ExecutionDetail';
import useDecisionOutcomes from './useDecisionOutcomes';
import Explanation from '../Explanation/Explanation';
import InputData from '../InputData/InputData';
import ModelLookup from '../ModelLookup/ModelLookup';
import './AuditDetail.scss';

const AuditDetail = () => {
  const { path, url } = useRouteMatch();
  const location = useLocation();
  const { executionId } = useParams<ExecutionRouteParams>();
  const execution = useExecutionInfo(executionId);
  const outcomes = useDecisionOutcomes(executionId);

  const [thirdLevelNav, setThirdLevelNav] = useState<
    { url: string; desc: string }[]
  >([]);

  useEffect(() => {
    if (outcomes.status === 'SUCCESS') {
      const newNav = [];
      if (outcomes.data.length === 1) {
        newNav.push({
          url: `/single-outcome`,
          desc: 'Outcome'
        });
      } else {
        newNav.push({ url: '/outcomes', desc: 'Outcomes' });
        newNav.push({ url: '/outcomes-details', desc: 'Outcomes Details' });
      }
      newNav.push({ url: '/input-data', desc: 'Input Data' });
      newNav.push({ url: '/model-lookup', desc: 'Model Lookup' });
      setThirdLevelNav(newNav);
    }
  }, [outcomes]);

  return (
    <>
      <PageSection variant={PageSectionVariants.light}>
        <ExecutionHeader execution={execution} />
        {thirdLevelNav.length === 0 && (
          <div className="audit-detail__nav">
            <SkeletonFlexStripes
              stripesNumber={4}
              stripesHeight={'1.5em'}
              stripesWidth={'120px'}
              isPadded={false}
            />
          </div>
        )}
        {thirdLevelNav.length > 0 && (
          <Nav className="audit-detail__nav" variant="tertiary">
            <NavList>
              {thirdLevelNav.map((item, index) => (
                <NavItem
                  key={`sub-nav-${index}`}
                  isActive={location.pathname === url + item.url}
                >
                  <Link to={url + item.url}>{item.desc}</Link>
                </NavItem>
              ))}
            </NavList>
          </Nav>
        )}
      </PageSection>

      <Switch>
        <Route path={`${path}/single-outcome`}>
          <Explanation outcomes={outcomes} />
        </Route>
        <Route path={`${path}/outcomes`}>
          <ExecutionDetail outcomes={outcomes} />
        </Route>
        <Route path={`${path}/outcomes-details`}>
          <Explanation outcomes={outcomes} />
        </Route>
        <Route path={`${path}/input-data`}>
          <InputData />
        </Route>
        <Route path={`${path}/model-lookup`}>
          <ModelLookup />
        </Route>
        <Route exact path={`${path}/`}>
          {outcomes.status === 'SUCCESS' && outcomes.data.length === 1 && (
            <Redirect
              exact
              from={path}
              to={`${location.pathname}/single-outcome?outcomeId=${outcomes.data[0].outcomeId}`}
            />
          )}
          {outcomes.status === 'SUCCESS' && outcomes.data.length > 1 && (
            <Redirect exact from={path} to={`${location.pathname}/outcomes`} />
          )}
          {outcomes.status === 'LOADING' && (
            <PageSection>
              <Stack hasGutter>
                <StackItem>
                  <SkeletonStripe
                    isInline={true}
                    customStyle={{ height: '1.5em' }}
                  />
                </StackItem>
                <StackItem>
                  <SkeletonCards quantity={2} />
                </StackItem>
              </Stack>
            </PageSection>
          )}
        </Route>
      </Switch>
    </>
  );
};

export default AuditDetail;
