/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react';
import { mount } from 'enzyme';
import HtmlFormRenderer from '../HtmlFormRenderer';

describe('HtmlFormRenderer test cases', () => {
  beforeAll(() => {
    const div = document.createElement('div');
    div.setAttribute('id', 'formContainer');
    document.body.appendChild(div);
  });
  it('Snapshot test with default props', () => {
    const props = {
      source:
        '<div>\n  <fieldset disabled>\n    <legend>Candidate</legend>\n    <div>\n      <div class="form-group">\n        <label for="uniforms-0000-0002">Email</label>\n        <input type="text" id="uniforms-0000-0002" name="candidate.email" class="form-control" disabled value="" />\n      </div>\n\n      <div class="form-group">\n        <label for="uniforms-0000-0003">Name</label>\n        <input type="text" id="uniforms-0000-0003" name="candidate.name" class="form-control" disabled value="" />\n      </div>\n\n      <div class="form-group">\n        <label for="uniforms-0000-0005">Salary</label>\n        <input type="number" class="form-control" id="uniforms-0000-0005" name="candidate.salary" disabled step="1" value="" />\n      </div>\n\n      <div class="form-group">\n        <label for="uniforms-0000-0006">Skills</label>\n        <input type="text" id="uniforms-0000-0006" name="candidate.skills" class="form-control" disabled value="" />\n      </div>\n    </div>\n  </fieldset>\n\n  <div class="form-check">\n    <input type="checkbox" id="uniforms-0000-0008" name="approve" class="form-check-input" />\n    <label class="form-check-label" for="uniforms-0000-0008">Approve</label>\n  </div>\n</div>\n',
      resources: {
        scripts: {
          'bootstrap.min.js':
            'https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js',
          'jquery.js': 'https://code.jquery.com/jquery-3.2.1.slim.min.js',
          'popper.js':
            'https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js'
        },
        styles: {
          'bootstrap.min.css':
            'https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css'
        }
      }
    };
    const wrapper = mount(<HtmlFormRenderer {...props} />);
    expect(wrapper).toMatchSnapshot();
    expect(wrapper.find('div')).toBeTruthy();
  });
  it('Test source with script tags', () => {
    const props = {
      source:
        '<script src=\'https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\'></script><div>\n  <fieldset disabled>\n    <legend>Candidate</legend>\n    <div>\n      <div class="form-group">\n        <label for="uniforms-0000-0002">Email</label>\n        <input type="text" id="uniforms-0000-0002" name="candidate.email" class="form-control" disabled value="" />\n      </div>\n\n      <div class="form-group">\n        <label for="uniforms-0000-0003">Name</label>\n        <input type="text" id="uniforms-0000-0003" name="candidate.name" class="form-control" disabled value="" />\n      </div>\n\n      <div class="form-group">\n        <label for="uniforms-0000-0005">Salary</label>\n        <input type="number" class="form-control" id="uniforms-0000-0005" name="candidate.salary" disabled step="1" value="" />\n      </div>\n\n      <div class="form-group">\n        <label for="uniforms-0000-0006">Skills</label>\n        <input type="text" id="uniforms-0000-0006" name="candidate.skills" class="form-control" disabled value="" />\n      </div>\n    </div>\n  </fieldset>\n\n  <div class="form-check">\n    <input type="checkbox" id="uniforms-0000-0008" name="approve" class="form-check-input" />\n    <label class="form-check-label" for="uniforms-0000-0008">Approve</label>\n  </div>\n</div>\n',
      resources: {
        scripts: {
          'bootstrap.min.js':
            'https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js',
          'jquery.js': 'https://code.jquery.com/jquery-3.2.1.slim.min.js',
          'popper.js':
            'https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js'
        },
        styles: {
          'bootstrap.min.css':
            'https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css'
        }
      }
    };
    const wrapper = mount(<HtmlFormRenderer {...props} />);
    expect(wrapper.find('div')).toBeTruthy();
  });
});
