import { useEffect, useState } from 'react';
import { debounce } from 'lodash';
type useCFTableSizesParameters = {
  containerSelector: string;
};

const useCFTableSizes = (parameters: useCFTableSizesParameters) => {
  const { containerSelector } = parameters;
  const [containerSize, setContainerSize] = useState(0);
  const [windowSize, setWindowSize] = useState();

  useEffect(() => {
    const getContainerSize = () => {
      const size = document.querySelector(containerSelector).clientWidth - 10;
      return size < 768 ? 768 : size;
    };

    setContainerSize(getContainerSize());
    setWindowSize(window.innerWidth);

    const handleWindowResize = debounce(() => {
      setContainerSize(getContainerSize);
      setWindowSize(window.innerWidth);
    }, 150);

    window.addEventListener('resize', handleWindowResize);
    return () => window.removeEventListener('resize', handleWindowResize);
  }, [containerSelector]);

  return { containerSize, windowSize };
};

export default useCFTableSizes;
