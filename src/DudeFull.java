import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude implements DynamicEntity {

    public DudeFull(String id,
                    Point position,
                    List<PImage> images,
                    int resourceLimit,
                    int resourceCount,
                    int actionPeriod,
                    int animationPeriod,
                    int health,
                    int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod,
                animationPeriod, health, healthLimit);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> fullTarget =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() && this.moveToFull(world,
                fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        DudeNotFull miner = EntityFactory.createDudeNotFull(this.id,
                this.position, this.actionPeriod,
                this.animationPeriod,
                this.resourceLimit,
                this.images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((ScheduledEntity)miner).scheduleActions(scheduler, world, imageStore);
    }

    private boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            return true;
        } else {
            Point nextPos = this.nextPositionDude(world, target.position);

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
}
