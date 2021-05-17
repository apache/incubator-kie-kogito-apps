import { useEffect, useState } from 'react';
import { debounce } from 'lodash';

type useCFTableSizesParameters = {
  headerSelector: string;
  wrapperSelector: string;
};

const useCFTableSizes = (parameters: useCFTableSizesParameters) => {
  const { headerSelector, wrapperSelector } = parameters;
  const [containerWidth, setContainerWidth] = useState(0);
  const [containerHeight, setContainerHeight] = useState(0);
  const [windowSize, setWindowSize] = useState();

  useEffect(() => {
    const getContainerWidth = () => {
      const size = document.querySelector(headerSelector).clientWidth - 10;
      return size < 768 ? 768 : size;
    };

    const getContainerHeight = () => {
      return document.querySelector(wrapperSelector).clientHeight;
    };

    setContainerWidth(getContainerWidth());
    setContainerHeight(getContainerHeight());
    setWindowSize(window.innerWidth);

    const handleWindowResize = debounce(() => {
      setContainerWidth(getContainerWidth());
      setContainerHeight(getContainerHeight());
      setWindowSize(window.innerWidth);
    }, 150);

    window.addEventListener('resize', handleWindowResize);
    return () => window.removeEventListener('resize', handleWindowResize);
  }, [headerSelector, wrapperSelector]);

  return { containerWidth, containerHeight, windowSize };
};

export default useCFTableSizes;
