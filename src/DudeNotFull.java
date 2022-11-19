import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude implements DynamicEntity{

    public DudeNotFull(String id,
                    Point position,
                    List<PImage> images,
                    int resourceLimit,
                    int resourceCount,
                    int actionPeriod,
                    int animationPeriod,
                    int health,
                    int healthLimit)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod,
                animationPeriod, health, healthLimit);
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !this.moveToNotFull(world,
                target.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    private boolean moveToNotFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.position)) {
            this.resourceCount += 1;
            target.health--;
            return true;
        }
        else {
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

    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            DudeFull miner = EntityFactory.createDudeFull(this.id,
                    this.position, this.actionPeriod,
                    this.animationPeriod,
                    this.resourceLimit,
                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((ScheduledEntity)miner).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}