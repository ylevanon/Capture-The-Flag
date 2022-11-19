import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Fairy extends Entity implements DynamicEntity, ScheduledEntity{

    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_NUM_PROPERTIES = 6;
    public static final int FAIRY_ID = 1;
    public static final int FAIRY_COL = 2;
    public static final int FAIRY_ROW = 3;
    public static final int FAIRY_ANIMATION_PERIOD = 4;
    public static final int FAIRY_ACTION_PERIOD = 5;

    public Fairy(       String id,
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
        Optional<Entity> fairyTarget =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (this.moveToFairy(world, fairyTarget.get(), scheduler)) {
                Entity sapling = EntityFactory.createSapling("sapling_" + this.id, tgtPos,
                        imageStore.getImageList(Sapling.SAPLING_KEY));

                world.addEntity(sapling);
                ((ScheduledEntity)sapling).scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    private Point nextPositionFairy(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    private boolean moveToFairy(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.position.adjacent(target.position)) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = this.nextPositionFairy(world, target.position);

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

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {                scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());}

}