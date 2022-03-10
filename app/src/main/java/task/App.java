package task;

import java.util.Arrays;

public class App {

    public static void main(String[] args) throws Exception {
        Driver driver = new Driver(Arrays.asList("James", "John", "Robert", "Michael", "William", "David", "Richard",
                "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth",
                "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy",
                "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory",
                "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl",
                "Arthur", "Ryan", "Roger"), 1000);
        driver.run("big.txt");
    }
}
