import java.io.File;

public class Main {

    public static void main(String[] args){
        //enter Maze Names
        String name = "small";
        Maze maze = new Maze(new Picture(new File("mazes/"+name+".png")));
        maze.check().save("Nodes/"+name+".png");
        System.out.printf("Load Factor: %f\n", maze.loadFactor());
        maze.generatePicture(maze.AStar()).save("Solved/"+name+".png");




    }

}
