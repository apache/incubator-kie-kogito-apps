import React, { ReactNode, useEffect, useState } from 'react';
import {
  Nav,
  NavItem,
  NavList,
  PageSection,
  PageSectionVariants,
  Stack,
  StackItem,
  Tooltip
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
import { ExecutionRouteParams, RemoteDataStatus } from '../../../types';
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
import Counterfactual from '../Counterfactual/Counterfactual';
import { OutlinedQuestionCircleIcon } from '@patternfly/react-icons';
import CounterfactualUnsupportedBanner from '../../Atoms/CounterfactualUnsupportedBanner/CounterfactualUnsupportedBanner';

const AuditDetail = () => {
  const { path, url } = useRouteMatch();
  const location = useLocation();
  const { executionId } = useParams<ExecutionRouteParams>();
  const execution = useExecutionInfo(executionId);
  const outcomes = useDecisionOutcomes(executionId);

  const [thirdLevelNav, setThirdLevelNav] = useState<
    { url: string; desc: string; icon?: ReactNode }[]
  >([]);

  useEffect(() => {
    if (outcomes.status === RemoteDataStatus.SUCCESS) {
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
      if (process.env.KOGITO_TRUSTY_COUNTERFACTUAL === 'enabled') {
        newNav.push({
          url: '/counterfactual-analysis',
          desc: 'Counterfactual Analysis',
          icon: (
            <Tooltip
              position="top"
              content={<div>Counterfactuals are an experimental feature.</div>}
            >
              <OutlinedQuestionCircleIcon title="An icon with a tooltip" />
            </Tooltip>
          )
        });
      }
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
          <>
            {process.env.KOGITO_TRUSTY_COUNTERFACTUAL === 'enabled' && (
              <CounterfactualUnsupportedBanner />
            )}
            <Nav
              className="audit-detail__nav"
              variant="tertiary"
              ouiaId="nav-audit-detail"
            >
              <NavList>
                {thirdLevelNav.map((item, index) => (
                  <NavItem
                    key={`sub-nav-${index}`}
                    isActive={location.pathname === url + item.url}
                    ouiaId={item.url.substr(1)}
                  >
                    <Link to={url + item.url}>
                      <>
                        {item.desc}
                        {item.icon && (
                          <div className={'audit-detail__nav__badge'}>
                            {item.icon}
                          </div>
                        )}
                      </>
                    </Link>
                  </NavItem>
                ))}
              </NavList>
            </Nav>
          </>
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
        {process.env.KOGITO_TRUSTY_COUNTERFACTUAL === 'enabled' && (
          <Route path={`${path}/counterfactual-analysis`}>
            <Counterfactual />
          </Route>
        )}
        <Route exact path={`${path}/`}>
          {outcomes.status === RemoteDataStatus.SUCCESS &&
            outcomes.data.length === 1 && (
              <Redirect
                exact
                from={path}
                to={`${location.pathname}/single-outcome?outcomeId=${outcomes.data[0].outcomeId}`}
              />
            )}
          {outcomes.status === RemoteDataStatus.SUCCESS &&
            outcomes.data.length > 1 && (
              <Redirect
                exact
                from={path}
                to={`${location.pathname}/outcomes`}
              />
            )}
          {outcomes.status === RemoteDataStatus.LOADING && (
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
