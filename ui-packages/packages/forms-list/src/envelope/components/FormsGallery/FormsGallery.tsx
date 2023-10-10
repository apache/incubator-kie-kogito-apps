import React from 'react';
import {
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { Bullseye } from '@patternfly/react-core/dist/js/layouts/Bullseye';
import {
  Gallery,
  GalleryItem
} from '@patternfly/react-core/dist/js/layouts/Gallery';
import { FormInfo } from '../../../api/FormsListEnvelopeApi';
import FormCard from '../FormCard/FormCard';
import { FormsListDriver } from '../../../api/FormsListDriver';
import {
  KogitoEmptyState,
  KogitoEmptyStateType
} from '@kogito-apps/components-common/dist/components/KogitoEmptyState';
import { KogitoSpinner } from '@kogito-apps/components-common/dist/components/KogitoSpinner';

export interface FormsGalleryProps {
  driver: FormsListDriver;
  formsData: FormInfo[];
  isLoading: boolean;
}

const FormsGallery: React.FC<FormsGalleryProps & OUIAProps> = ({
  driver,
  formsData,
  isLoading,
  ouiaId,
  ouiaSafe
}) => {
  if (isLoading) {
    return (
      <Bullseye>
        <KogitoSpinner
          spinnerText="Loading forms..."
          ouiaId="forms-list-loading-forms"
        />
      </Bullseye>
    );
  }

  if (!isLoading && formsData && formsData.length === 0) {
    return (
      <KogitoEmptyState
        type={KogitoEmptyStateType.Search}
        title="No results found"
        body="Try using different filters"
      />
    );
  }

  return (
    <Gallery
      hasGutter
      style={{ margin: '25px' }}
      {...componentOuiaProps(ouiaId, 'forms-gallery', ouiaSafe)}
    >
      {formsData &&
        formsData.map((formData, index) => (
          <GalleryItem key={index}>
            <FormCard
              formData={formData}
              key={index}
              driver={driver}
            ></FormCard>
          </GalleryItem>
        ))}
    </Gallery>
  );
};

export default FormsGallery;
