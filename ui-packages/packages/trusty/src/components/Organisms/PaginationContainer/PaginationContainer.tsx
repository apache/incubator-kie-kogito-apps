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
import { Pagination, PaginationVariant } from '@patternfly/react-core';

type PaginationContainerProps = {
  total: number;
  page: number;
  pageSize: number;
  paginationId: string;
  onSetPage: (page: number) => void;
  onSetPageSize: (size: number) => void;
};

const PaginationContainer = (props: PaginationContainerProps) => {
  const {
    total,
    page,
    pageSize,
    paginationId,
    onSetPage,
    onSetPageSize
  } = props;

  const updatePage = (event: any, pageNumber: number) => {
    onSetPage(pageNumber);
  };
  const updatePageSize = (event: any, pageNumber: number) => {
    onSetPageSize(pageNumber);
  };
  return (
    <Pagination
      itemCount={total}
      perPage={pageSize}
      page={page}
      onSetPage={updatePage}
      widgetId={paginationId}
      onPerPageSelect={updatePageSize}
      variant={PaginationVariant.top}
    />
  );
};

export default PaginationContainer;
