import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class House extends Entity
{

    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 4;
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

//    public void executeActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler)
//    {
//        Optional<Entity> turretTarget =
//                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Dude.class)));
//
//        if (turretTarget.isPresent()) {
//            Point tgtPos = turretTarget.get().position;
//
//            if (this.moveToFairy(world, fairyTarget.get(), scheduler)) {
//                Entity sapling = EntityFactory.createSapling("sapling_" + this.id, tgtPos,
//                        imageStore.getImageList(Sapling.SAPLING_KEY));
//
//                world.addEntity(sapling);
//                ((ScheduledEntity)sapling).scheduleActions(scheduler, world, imageStore);
//            }
//        }
//
//        scheduler.scheduleEvent(this,
//                this.createActivityAction(world, imageStore),
//                this.actionPeriod);
//    }


}
