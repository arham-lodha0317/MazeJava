import java.io.File;

public class Main {

    public static void main(String[] args){

        Maze maze = new Maze(new Picture(new File("mazes/braid200.png")));
        maze.check().save("check.png");
        System.out.printf("Load Factor: %f\n", maze.loadFactor());
        maze.generatePicture(maze.AStar()).save("check2.png");




    }

}
