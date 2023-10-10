import React, { useState, useEffect } from 'react';
import {
  Card,
  CardHeader,
  CardBody
} from '@patternfly/react-core/dist/js/components/Card';
import { Tooltip } from '@patternfly/react-core/dist/js/components/Tooltip';
import { Title } from '@patternfly/react-core/dist/js/components/Title';
import {
  Table,
  TableVariant,
  TableHeader,
  TableBody,
  IRow,
  ICell
} from '@patternfly/react-table/dist/js/components/Table';
import Moment from 'react-moment';
import JobActionsKebab from '../JobActionsKebab/JobActionsKebab';
import {
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { Job } from '@kogito-apps/management-console-shared/dist/types';
import { JobsIconCreator } from '../../../utils/Utils';
import { ProcessDetailsDriver } from '../../../api';

interface JobsPanelProps {
  jobs: Job[];
  driver: ProcessDetailsDriver;
}

const JobsPanel: React.FC<JobsPanelProps & OUIAProps> = ({
  jobs,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const [rows, setRows] = useState<IRow[]>([]);

  const columns: ICell[] = [
    {
      title: 'Job id'
    },
    {
      title: 'Status'
    },
    {
      title: 'Expiration time'
    },
    {
      title: 'Actions'
    }
  ];

  const createRows = (jobsArray: Job[]): IRow[] => {
    const jobRows = [];
    jobsArray.forEach((job) => {
      jobRows.push({
        cells: [
          {
            title: (
              <Tooltip content={job.id}>
                <span>{job.id.substring(0, 7)}</span>
              </Tooltip>
            )
          },
          {
            title: JobsIconCreator(job.status)
          },
          {
            title: (
              <React.Fragment>
                {job.expirationTime ? (
                  <>
                    {' '}
                    expires in{' '}
                    <Moment fromNow ago>
                      {job.expirationTime}
                    </Moment>
                  </>
                ) : (
                  'N/A'
                )}
              </React.Fragment>
            )
          },
          {
            title: <JobActionsKebab job={job} driver={driver} />
          }
        ]
      });
    });
    return jobRows;
  };

  useEffect(() => {
    if (jobs.length > 0) {
      setRows(createRows(jobs));
    }
  }, [jobs]);

  if (jobs.length > 0) {
    return (
      <Card
        {...componentOuiaProps(
          ouiaId,
          'process-details-jobs-panel'
          // ouiaSafe ? ouiaSafe : !jobsResponse.loading
        )}
      >
        <CardHeader>
          <Title headingLevel="h3" size="xl">
            Jobs
          </Title>
        </CardHeader>
        <CardBody>
          <Table
            aria-label="Process details jobs panel"
            aria-labelledby="Process details jobs panel"
            variant={TableVariant.compact}
            rows={rows}
            cells={columns}
          >
            <TableHeader />
            <TableBody />
          </Table>
        </CardBody>
      </Card>
    );
  } else {
    return null;
  }
};

export default JobsPanel;
