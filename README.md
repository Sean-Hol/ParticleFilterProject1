# ParticleFilterProject1

This is an example of a particle filter used for object tracking and a scenario to go with it.
The filter only knows the distance to the closest landmark/beacon and the movement vector for each step (both with some noise)


The program will open a canvas Jframe for every step of the filter. The canvas will be 1200 x 800 pixels. The object can only be in black areas, assume white areas are walls.
The red dot signifies the object, although the object is only a single coordinate (at the centre of the dot), it is 30pixels in diameter so it can be clearly seen.
Green dots signify landmarks/beacons, and the object can sense its distance to the landmark/beacon closest to it.

(In this version, the particle weight carry over, and new weights are multiplied with the current weights. It seems to create a more accurate result)
