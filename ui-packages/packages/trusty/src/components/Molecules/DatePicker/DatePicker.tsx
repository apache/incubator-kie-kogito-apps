import React, { useEffect, useRef } from 'react';
import { InputGroup, TextInput } from '@patternfly/react-core';
import flatpickr from 'flatpickr';
import { Instance as flatpickrInstance } from 'flatpickr/dist/types/instance';
import './DatePicker.scss';

type DatePickerProps = {
  fromDate?: string;
  id: string;
  label?: string;
  minDate?: string;
  maxDate?: string;
  onDateUpdate: (date: string) => void;
  value?: string;
};

const DatePicker = (props: DatePickerProps) => {
  const { fromDate, minDate, maxDate, value, onDateUpdate, id, label } = props;
  const datePicker = useRef<HTMLInputElement>(null);

  useEffect(() => {
    let calendar: flatpickrInstance;
    const onChange = (selectedDates: Date[], dateStr: string) => {
      onDateUpdate(dateStr);
    };
    if (datePicker && datePicker.current) {
      calendar = flatpickr(datePicker.current, {
        allowInput: true,
        altInput: true,
        altFormat: 'F j, Y',
        dateFormat: 'Y-m-d',
        defaultDate: value,
        minDate,
        maxDate,
        monthSelectorType: 'static',
        onChange,
        prevArrow: leftArrow,
        nextArrow: rightArrow,
        static: true
      });
    }
    return () => {
      calendar.destroy();
    };
  }, [fromDate, maxDate, onDateUpdate, minDate, value, id]);

  return (
    <InputGroup>
      <TextInput
        name={id}
        id={id}
        type="date"
        aria-label={label}
        ref={datePicker}
      />
    </InputGroup>
  );
};

export default DatePicker;

flatpickr.l10ns.en.weekdays.shorthand.forEach((day, index, daysArray) => {
  if (daysArray[index] === 'Thu' || daysArray[index] === 'Th') {
    daysArray[index] = 'Th';
  } else {
    daysArray[index] = daysArray[index].charAt(0);
  }
});

const leftArrow = `<svg fill="currentColor" height="1em" width="1em" viewBox="0 0 256 512" aria-hidden="true" role="img" style="vertical-align: -0.125em;"><path d="M31.7 239l136-136c9.4-9.4 24.6-9.4 33.9 0l22.6 22.6c9.4 9.4 9.4 24.6 0 33.9L127.9 256l96.4 96.4c9.4 9.4 9.4 24.6 0 33.9L201.7 409c-9.4 9.4-24.6 9.4-33.9 0l-136-136c-9.5-9.4-9.5-24.6-.1-34z" transform=""/></svg>`;
const rightArrow = `<svg fill="currentColor" height="1em" width="1em" viewBox="0 0 256 512" aria-hidden="true" role="img" style="vertical-align: -0.125em;"><path d="M224.3 273l-136 136c-9.4 9.4-24.6 9.4-33.9 0l-22.6-22.6c-9.4-9.4-9.4-24.6 0-33.9l96.4-96.4-96.4-96.4c-9.4-9.4-9.4-24.6 0-33.9L54.3 103c9.4-9.4 24.6-9.4 33.9 0l136 136c9.5 9.4 9.5 24.6.1 34z" transform=""/></svg>`;
