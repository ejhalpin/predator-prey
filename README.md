# predator-prey
A basic predator-prey model
PredPrey is a visual simulation engine. It is intended for entertainment purposes only.

The simulation occurs over a finite number of time steps, during which each cell in the habitat is evolved. Because the behavior of a given cell depends on the surrounding cells, it is necessary to mark a given cell as having been evolved. The simulation engine gathers a list of the occupants of the habitat. The list is randomized and iterated through, with each occupant given the opportunity to eat, breed, and move (if the occupant is a predator), or eat and spawn (if the occupant is prey).
*****
In this simulation, the following constructs of predator and prey have been used in developing the simulation engine.
*****
*****
Predator -> a single sexless entity that occupies an entire cell. If a predator is in a cell, no other entity may occupy that cell for any period of time. All predators abide by the same rules and those rules are governed by user-defined stats (Hunt Proficiency, Breed Proficiency, Decay Rate, etc.). When a predator leaves a cell, that cell becomes empty and may, during the same time step, be occupied by any other entity. Predators do not mark their territory. During the evolution process, a selected predator will attempt to:

eat -> this action is only permitted if a nearest neighboring cell contains prey. Predators have a limited range. The action is performed by the comparison of a randomly generated decimal value, r between 0 and 1 (inclusive and exclusive, respectively) and the Hunt Proficiency stat. A successful hunt requires that r <= hunt proficiency. The outcomes of this comparison have the following effect:

	true -> the predator will consume some or all of the prey in the neighboring cell. The amount of health acquired by the predator cannot exceed the health of the selected prey. The prey is chosen at random from a list of prey that occupy neighboring cells. Predators do not select their prey. If a cell containing prey is reduced to a health of 0, the cell becomes empty. 

	false -> The predator does not eat and its health is decremented by the value of Decay Rate.

breed -> this action is only permitted if the nearest neighboring cells contain both another predator and an empty cell. The action is performed by the comparison of a randomly generated decimal value, r between 0 and 1 (inclusive and exclusive, respectively) and the Breed Proficiency stat. A successful breeding will result in both predators being marked as having bred and the generation of a new predator in an empty neighboring cell. The new predator will be marked as having eaten during the time step in which it was born. It may interact with other predators during the same time step. 

move -> if the neighboring cells contain empty space, the predator will move into a randomly chosen vacant cell. Predators are regionally nomadic, however given the nature of cell selection it is equally likely that a predator will remain in a given region than venture out of it. A deeper examination of the mobility of a predator is warranted.
*****
Prey -> a community of non-aggressive entities that do not require additional cell interaction in order to breed. The health of a prey cell is analogous to the population of prey within the cell. For this reason, a cap on the health of prey must be set by the user such that when the health of a prey cell reaches or exceed the maximum value, an event is triggered which determines the outcome of overpopulation. During the evolution process, a selected pry cell will:

eat -> the habitat, which consists of a finite number of cells, has the potential to support a given amount of life. The potential for a selected prey to eat depends on the average prey population per cell (the average health of the prey population) and the flora multiplier. A prey cell will eat if a comparison between a randomly generated decimal value, r between 0 and 1 (inclusive and exclusive, respectively) and the computed value of (1-need/food) occurs favorably. That is, r < (1-need/food), where need is the sum of all of the health agues of all prey cells in the grid, and food is the total number of cells in the grid multiplied by the flora value. 

spawn-> if the max health of a prey cell is met or exceeded and there is at least one empty cell within the nearest neighboring cells, then a new prey cell is created in a randomly chosen empty cell and the current cell’s population (health) is reduced to the instantiation value. If there is no empty space in the neighboring cells, the entire population of the current cell dies (colony collapse). 
*****
*****
The evolution process continues by iterating through each of the cells of the habitat and decrementing the health of any entity that did not eat during its turn. The decrementing amount is set by the user for predators and prey: Health Decay Rate. After this occurs, the health and age of each cell is checked. If the health of a cell is <=0 or the age of an entity is >= the maximum age, the cell entity is destroyed and the cell becomes empty. Lastly, each entity within the habitat is aged by 1.
*****
The simulation collects data about the population of each entity type, and a graph is auto-generated and displayed for the user. In addition, an animation is generated that shows the habitat populations at the end of each time-step. 
*****
