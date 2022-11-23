import processing.core.PImage;

import java.util.List;


public class Laser extends Entity implements DynamicEntity, ScheduledEntity{


    public static final String LASER_KEY = "laser";
    public static final int LASER_NUM_PROPERTIES = 10;
    public static final int LASER_ID = 1;
    public static final int LASER_COL = 2;
    public static final int LASER_ROW = 3;
    public static final int LASER_ANIMATION_PERIOD = 4;
    private final int direction;
    public static final int LASER_ACTION_PERIOD = 5;

    public Laser( String id,
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
        this.direction = Laser.calculateDirection(position);
    // need to get nearest player and make calculation
                //direction;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point nextPos = this.nextPositionLaser(world);
        if (!nextPos.equals(this.position))
        {
            world.moveEntity(this, nextPos);
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
        else
        {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
        }

    }

//    private Point moveToLaser(WorldModel world)
//    {
////        if (this.position.adjacent(target.position)) {
////            world.removeEntity(target);
////            scheduler.unscheduleAllEvents(target);
////            return true;
////        }
//            Point nextPos = this.nextPositionLaser(world);
//
//            if (!this.position.equals(nextPos)) {
////                Optional<Entity> occupant = world.getOccupant(nextPos);
////                if (occupant.isPresent()) {
////                    scheduler.unscheduleAllEvents(occupant.get());
////                }
//                return nextPos;
//
//            }
//            return this.position;
//    }

    private Point nextPositionLaser(WorldModel world)
    {
        Point destPos;

            if (this.direction == 0)
            {
                destPos = new Point(this.position.getX() + 1, this.position.getY());
            }
            else if (this.direction == 1)
            {
                destPos = new Point(this.position.getX() + 1, this.position.getY() - 1);
            }
            else if (this.direction == 2)
            {
                destPos = new Point(this.position.getX(), this.position.getY() - 1);
            }
            else if (this.direction == 3)
            {
                destPos = new Point(this.position.getX() - 1, this.position.getY() - 1);
            }
            else if (this.direction == 4)
            {
                destPos = new Point(this.position.getX() - 1, this.position.getY());
            }
            else if (this.direction == 5)
            {
                destPos = new Point(this.position.getX() - 1, this.position.getY() + 1);
            }
            else if (this.direction == 6)
            {
                destPos = new Point(this.position.getX(), this.position.getY() + 1);
            }
            else {
        destPos = new Point(this.position.getX() + 1, this.position.getY() + 1);
    }
            if (withinBounds(destPos, world) && !(world.isOccupied(destPos)))
                return destPos;
            else
                return this.position;
    }

    private static boolean withinBounds(Point p, WorldModel world)
    {
        return p.getY() >= 0 && p.getY() < world.getNumCols() &&
                p.getX() >= 0 && p.getX() < world.getNumRows();
    }

    private static boolean neighbors(Point p1, Point p2)
    {
        return p1.getX()+1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX()-1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()+1 == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY() - 1 == p2.getY() ||
                p1.getX() + 1 == p2.getX() && p1.getY() + 1 == p2.getY() ||
                p1.getX() - 1 == p2.getX() && p1.getY() - 1 == p2.getY() ||
                p1.getX() - 1 == p2.getX() && p1.getY() + 1 == p2.getY() ||
                p1.getX() + 1 == p2.getX() && p1.getY() - 1 == p2.getY();
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore){
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }

    public static int calculateDirection(Point position)
    {
//        double dist1 = Math.abs(VirtualWorld.p1X - position.getX()) + Math.abs(VirtualWorld.p1Y - position.getY());
//        double dist2 = Math.abs(VirtualWorld.p2X - position.getX()) + Math.abs(VirtualWorld.p2Y - position.getY());
//        double x, y;
//        if (dist1 <= dist2)
//        {
//            x = VirtualWorld.p1X;
//            y = VirtualWorld.p1Y;
//        }
//        else
//        {
//            x = VirtualWorld.p2X;
//            y = VirtualWorld.p2Y;
//        }

        double x = VirtualWorld.p1X;
        double y = VirtualWorld.p1Y;

        double angle = (180/Math.PI) * Math.atan(((y - position.getY()) / (x - position.getX())));
        if (angle > (0 % 360) && angle <= (45 % 360))
            return 0;
        else if (angle > (45 % 360) && angle <= (90 % 360))
            return 1;
        else if (angle > (90 % 360) && angle <= (135 % 360))
            return 2;
        else if (angle > (135 % 360) && angle <= (180 % 360))
            return 3;
        else if (angle > (180 % 360) && angle <= (225 % 360))
            return 4;
        else if (angle > (225 % 360) && angle <= (270 % 360))
            return 5;
        else if (angle > (270 % 360) && angle <= (315 % 360))
            return 6;
        else
            return 7;


    }
}
