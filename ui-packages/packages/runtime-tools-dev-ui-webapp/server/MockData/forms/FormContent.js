module.exports = [
    {
        name: "form1",
        source: {
            sourceContent: '<div> <div class="form-check"> <input type="checkbox" id="uniforms-0001-0001" name="approve" class="form-check-input" /> <label class="form-check-label" for="uniforms-0001-0001">Approve</label> </div> <fieldset disabled> <legend>Candidate</legend> <div> <div class="form-group"> <label for="uniforms-0001-0004">Email</label> <input type="text" id="uniforms-0001-0004" name="candidate.email" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0005">Name</label> <input type="text" id="uniforms-0001-0005" name="candidate.name" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0007">Salary</label> <input type="number" class="form-control" id="uniforms-0001-0007" name="candidate.salary" disabled step="1" value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0008">Skills</label> <input type="text" id="uniforms-0001-0008" name="candidate.skills" class="form-control" disabled value="" /> </div> </div> </fieldset> </div>'
        },
        formConfiguration: {
            resources: {
                styles: {
                    1: "2"
                },
                "scripts": {}
            },
            schema: '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"approve":{"type":"boolean","output":true},"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true}}}'
        }
    },
    {
        name: "form2",
        source: {
            sourceContent: `import React, { useState } from 'react';
            import {
              Card,
              CardBody,
              TextInput,
              FormGroup,
              Checkbox,
            } from '@patternfly/react-core';
            
            const Form__hiring_HRInterview: React.FC<any> = (props: any) => {
              const [candidate__email, set__candidate__email] = useState<string>();
              const [candidate__name, set__candidate__name] = useState<string>();
              const [candidate__salary, set__candidate__salary] = useState<string>();
              const [candidate__skills, set__candidate__skills] = useState<string>();
              const [approve, set__approve] = useState<boolean>();
            
              return (
                <div className={'pf-c-form'}>
                  <Card>
                    <CardBody className='pf-c-form'>
                      <label>
                        <b>Candidate</b>
                      </label>
                      <FormGroup
                        fieldId={'uniforms-0000-0002'}
                        label={'Email'}
                        isRequired={false}>
                        <TextInput
                          name={'candidate.email'}
                          id={'uniforms-0000-0002'}
                          isDisabled={true}
                          placeholder={''}
                          type={'text'}
                          value={candidate__email}
                          onChange={set__candidate__email}
                        />
                      </FormGroup>
                      <FormGroup
                        fieldId={'uniforms-0000-0003'}
                        label={'Name'}
                        isRequired={false}>
                        <TextInput
                          name={'candidate.name'}
                          id={'uniforms-0000-0003'}
                          isDisabled={true}
                          placeholder={''}
                          type={'text'}
                          value={candidate__name}
                          onChange={set__candidate__name}
                        />
                      </FormGroup>
                      <FormGroup
                        fieldId={'uniforms-0000-0005'}
                        label={'Salary'}
                        isRequired={false}>
                        <TextInput
                          type={'number'}
                          name={'candidate.salary'}
                          isDisabled={true}
                          id={'uniforms-0000-0005'}
                          placeholder={''}
                          step={1}
                          value={candidate__salary}
                          onChange={set__candidate__salary}
                        />
                      </FormGroup>
                      <FormGroup
                        fieldId={'uniforms-0000-0006'}
                        label={'Skills'}
                        isRequired={false}>
                        <TextInput
                          name={'candidate.skills'}
                          id={'uniforms-0000-0006'}
                          isDisabled={true}
                          placeholder={''}
                          type={'text'}
                          value={candidate__skills}
                          onChange={set__candidate__skills}
                        />
                      </FormGroup>
                    </CardBody>
                  </Card>
                  <FormGroup fieldId='uniforms-0000-0008'>
                    <Checkbox
                      isChecked={approve}
                      isDisabled={false}
                      id={'uniforms-0000-0008'}
                      name={'approve'}
                      label={'Approve'}
                      onChange={set__approve}
                    />
                  </FormGroup>
                </div>
              );
            };
            
            export default Form__hiring_HRInterview;`
        },
        formConfiguration: {
            resources: {
                styles: {
                    1: "2"
                },
                "scripts": {}
            },
            schema: '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"approve":{"type":"boolean","output":true},"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true}}}'
        }
    },
    {

        name: "form3",
        source: {
            sourceContent: '<div> <div class="form-check"> <input type="checkbox" id="uniforms-0001-0001" name="approve" class="form-check-input" /> <label class="form-check-label" for="uniforms-0001-0001">Approve</label> </div> <fieldset disabled> <legend>Candidate</legend> <div> <div class="form-group"> <label for="uniforms-0001-0004">Email</label> <input type="text" id="uniforms-0001-0004" name="candidate.email" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0005">Name</label> <input type="text" id="uniforms-0001-0005" name="candidate.name" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0007">Salary</label> <input type="number" class="form-control" id="uniforms-0001-0007" name="candidate.salary" disabled step="1" value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0008">Skills</label> <input type="text" id="uniforms-0001-0008" name="candidate.skills" class="form-control" disabled value="" /> </div> </div> </fieldset> </div>'
        },
        formConfiguration: {
            resources: {
                styles: {
                    1: "2"
                },
                "scripts": {}
            },
            schema: '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"approve":{"type":"boolean","output":true},"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true}}}'
        }
    },
    {
        name: "form4",
        source: {
            sourceContent: '<div> <div class="form-check"> <input type="checkbox" id="uniforms-0001-0001" name="approve" class="form-check-input" /> <label class="form-check-label" for="uniforms-0001-0001">Approve</label> </div> <fieldset disabled> <legend>Candidate</legend> <div> <div class="form-group"> <label for="uniforms-0001-0004">Email</label> <input type="text" id="uniforms-0001-0004" name="candidate.email" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0005">Name</label> <input type="text" id="uniforms-0001-0005" name="candidate.name" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0007">Salary</label> <input type="number" class="form-control" id="uniforms-0001-0007" name="candidate.salary" disabled step="1" value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0008">Skills</label> <input type="text" id="uniforms-0001-0008" name="candidate.skills" class="form-control" disabled value="" /> </div> </div> </fieldset> </div>'
        },
        formConfiguration: {
            resources: {
                styles: {
                    1: "2"
                },
                "scripts": {}
            },
            schema: '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"approve":{"type":"boolean","output":true},"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true}}}'
        }
    },
    {
        name: "form5",
        source: {
            sourceContent: '<div> <div class="form-check"> <input type="checkbox" id="uniforms-0001-0001" name="approve" class="form-check-input" /> <label class="form-check-label" for="uniforms-0001-0001">Approve</label> </div> <fieldset disabled> <legend>Candidate</legend> <div> <div class="form-group"> <label for="uniforms-0001-0004">Email</label> <input type="text" id="uniforms-0001-0004" name="candidate.email" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0005">Name</label> <input type="text" id="uniforms-0001-0005" name="candidate.name" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0007">Salary</label> <input type="number" class="form-control" id="uniforms-0001-0007" name="candidate.salary" disabled step="1" value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0008">Skills</label> <input type="text" id="uniforms-0001-0008" name="candidate.skills" class="form-control" disabled value="" /> </div> </div> </fieldset> </div>'
        },
        formConfiguration: {
            resources: {
                styles: {
                    1: "2"
                },
                "scripts": {}
            },
            schema: '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"approve":{"type":"boolean","output":true},"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true}}}'
        }
    },
    {
        name: "form6",
        source: {
            sourceContent: '<div> <div class="form-check"> <input type="checkbox" id="uniforms-0001-0001" name="approve" class="form-check-input" /> <label class="form-check-label" for="uniforms-0001-0001">Approve</label> </div> <fieldset disabled> <legend>Candidate</legend> <div> <div class="form-group"> <label for="uniforms-0001-0004">Email</label> <input type="text" id="uniforms-0001-0004" name="candidate.email" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0005">Name</label> <input type="text" id="uniforms-0001-0005" name="candidate.name" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0007">Salary</label> <input type="number" class="form-control" id="uniforms-0001-0007" name="candidate.salary" disabled step="1" value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0008">Skills</label> <input type="text" id="uniforms-0001-0008" name="candidate.skills" class="form-control" disabled value="" /> </div> </div> </fieldset> </div>'
        },
        formConfiguration: {
            resources: {
                styles: {
                    1: "2"
                },
                "scripts": {}
            },
            schema: '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"approve":{"type":"boolean","output":true},"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true}}}'
        }
    },
    {
        name: "form7",
        source: {
            sourceContent: '<div> <div class="form-check"> <input type="checkbox" id="uniforms-0001-0001" name="approve" class="form-check-input" /> <label class="form-check-label" for="uniforms-0001-0001">Approve</label> </div> <fieldset disabled> <legend>Candidate</legend> <div> <div class="form-group"> <label for="uniforms-0001-0004">Email</label> <input type="text" id="uniforms-0001-0004" name="candidate.email" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0005">Name</label> <input type="text" id="uniforms-0001-0005" name="candidate.name" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0007">Salary</label> <input type="number" class="form-control" id="uniforms-0001-0007" name="candidate.salary" disabled step="1" value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0008">Skills</label> <input type="text" id="uniforms-0001-0008" name="candidate.skills" class="form-control" disabled value="" /> </div> </div> </fieldset> </div>'
        },
        formConfiguration: {
            resources: {
                styles: {
                    1: "2"
                },
                "scripts": {}
            },
            schema: '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"approve":{"type":"boolean","output":true},"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true}}}'
        }
    },
    {
        name: "form8",
        source: {
            sourceContent: '<div> <div class="form-check"> <input type="checkbox" id="uniforms-0001-0001" name="approve" class="form-check-input" /> <label class="form-check-label" for="uniforms-0001-0001">Approve</label> </div> <fieldset disabled> <legend>Candidate</legend> <div> <div class="form-group"> <label for="uniforms-0001-0004">Email</label> <input type="text" id="uniforms-0001-0004" name="candidate.email" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0005">Name</label> <input type="text" id="uniforms-0001-0005" name="candidate.name" class="form-control" disabled value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0007">Salary</label> <input type="number" class="form-control" id="uniforms-0001-0007" name="candidate.salary" disabled step="1" value="" /> </div> <div class="form-group"> <label for="uniforms-0001-0008">Skills</label> <input type="text" id="uniforms-0001-0008" name="candidate.skills" class="form-control" disabled value="" /> </div> </div> </fieldset> </div>'
        },
        formConfiguration: {
            resources: {
                styles: {
                    1: "2"
                },
                "scripts": {}
            },
            schema: '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"approve":{"type":"boolean","output":true},"candidate":{"type":"object","properties":{"email":{"type":"string"},"name":{"type":"string"},"salary":{"type":"integer"},"skills":{"type":"string"}},"input":true}}}'
        }
    }
]