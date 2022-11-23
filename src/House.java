import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class House extends Entity implements DynamicEntity, ScheduledEntity
{

    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_ACTION_PERIOD = 4;
    public static final int HOUSE_NUM_PROPERTIES = 5;
    public static final int HOUSE_ID = 1;
    public static final int HOUSE_COL = 2;
    public static final int HOUSE_ROW = 3;



    public House (String id,
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
        Optional<Entity> turretTarget =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(DudeFull.class, DudeNotFull.class)));

        if (turretTarget.isPresent()) {
            Point tgtPos = turretTarget.get().position;
//            {
//                return new Laser(id, position, images, 0, 0,
//                        actionPeriod, animationPeriod, 0, 0);
//            }

            Entity laser = EntityFactory.createLaser("laser_" + this.id, this.position,
                    Laser.LASER_ACTION_PERIOD,
                    Laser.LASER_ANIMATION_PERIOD,
                    imageStore.getImageList(Fairy.FAIRY_KEY));

                world.addEntity(laser);
                ((ScheduledEntity)laser).scheduleActions(scheduler, world, imageStore);

        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
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
