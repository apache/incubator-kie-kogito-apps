import React, { ReactElement } from 'react';
import { Helmet } from 'react-helmet';
import { FormResources } from '../../../api';

interface ResourcesContainerProps {
  resources: FormResources;
}

const ResourcesContainer: React.FC<ResourcesContainerProps> = ({
  resources
}) => {
  const scriptTags: ReactElement[] = [];
  const styleTags: ReactElement[] = [];

  if (resources) {
    for (const key in resources.styles) {
      styleTags.push(
        <link rel={'stylesheet'} href={resources.styles[key]} key={key} />
      );
    }
    for (const key in resources.scripts) {
      scriptTags.push(
        <script
          src={resources.scripts[key]}
          type={'text/javascript'}
          async={false}
          key={key}
        />
      );
    }
  }

  return (
    <Helmet>
      {styleTags}
      {scriptTags}
    </Helmet>
  );
};

export default ResourcesContainer;
