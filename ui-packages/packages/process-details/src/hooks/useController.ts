import { useCallback, useState } from 'react';

export function useController<T>(): [T | undefined, (controller: T) => void] {
  const [controller, setController] = useState<T | undefined>(undefined);

  const ref = useCallback((controller: T) => {
    setController(controller);
  }, []);

  return [controller, ref];
}
