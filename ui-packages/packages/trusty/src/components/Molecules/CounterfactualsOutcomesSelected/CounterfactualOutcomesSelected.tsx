import React, { useEffect, useState } from 'react';
import { CFGoal } from '../../Templates/Counterfactual/Counterfactual';
import { List, ListItem, ListVariant } from '@patternfly/react-core';

type CounterfactualOutcomesSelectedProps = {
  goals: CFGoal[];
};

const CounterfactualOutcomesSelected = ({
  goals
}: CounterfactualOutcomesSelectedProps) => {
  const [selectedOutcomes, setSelectedOutcomes] = useState<CFGoal[]>([]);

  useEffect(() => {
    setSelectedOutcomes(goals.filter(goal => !goal.isFixed));
  }, [goals]);

  return (
    <>
      {selectedOutcomes.length > 0 && (
        <List
          variant={ListVariant.inline}
          style={{ color: 'var(--pf-global--Color--200)' }}
        >
          <ListItem key="selected outcomes">
            <span>Selected Outcomes</span>
          </ListItem>
          {selectedOutcomes.map(goal => (
            <ListItem key={goal.id}>
              {goal.name}: {goal.value.toString()}
            </ListItem>
          ))}
        </List>
      )}
    </>
  );
};

export default CounterfactualOutcomesSelected;
