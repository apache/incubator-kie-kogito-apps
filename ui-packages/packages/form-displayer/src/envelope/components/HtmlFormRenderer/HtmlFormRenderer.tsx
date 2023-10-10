import React from 'react';
import InnerHTML from 'dangerously-set-html-content';
import { FormResources } from '../../../api';
import ResourcesContainer from '../ResourcesContainer/ResourcesContainer';

interface HtmlFormRendererProps {
  source: string;
  resources: FormResources;
}

const HtmlFormRenderer: React.FC<HtmlFormRendererProps> = ({
  source,
  resources
}) => {
  return (
    <div id="formContainer">
      <ResourcesContainer resources={resources} />
      <InnerHTML html={source} />
    </div>
  );
};

export default HtmlFormRenderer;
