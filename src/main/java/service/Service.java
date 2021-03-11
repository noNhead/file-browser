package service;

import java.io.*;
import java.util.*;

public class Service {
    String PATH = "C:/Code/tests/tests";

    public void lifecycleApp(){
        do {
            Scanner in = new Scanner(System.in);
            String line = in.nextLine();
            String[] strings = line.split(" ");
            switch (strings[0]) {
                case ("ls"):
                    if (strings.length>1) ls(strings[1]);
                    else ls("default");
                    break;
                case ("delete"):
                    delete(strings[1]);
                    break;
                case ("create"):
                    try {
                        create(strings[1]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case ("copy"):
                    try {
                        copyWithStream(strings[1], strings[2]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case("exit"):
                    System.exit(0);
            }
        } while (true);
    }

    public void ls(String suffix){
        File file = new File(PATH);
        if (file.isFile()){
            System.out.println("the address is sent to the file");
            return;
        }
        List<File> files = Arrays.asList(Objects.requireNonNull(file.listFiles()));
        switch(suffix){
            case("/size-asc"):
                files.sort(Comparator.comparingLong(File::getTotalSpace));
                break;
            case("/size-desc"):
                files.sort((o1, o2) -> Long.compare(o2.getTotalSpace(), o1.getTotalSpace()));
                break;
            case ("/desc"):
                files.sort(Comparator.comparing(File::toString, Comparator.reverseOrder()));
                break;
        }
        for (File value: files) {
            System.out.println(value.toString() + " " + value.getTotalSpace());
        }
    }

    public void delete(String string){
        File file = new File(PATH + "/" + string);
        if (file.exists()){
            if(file.delete()) System.out.println("the file has been deleted");
            else System.out.println("the file was not deleted due to an unknown reason");
        } else {
            System.out.println("file does not exist");
        }
    }

    public void create(String string) throws IOException {
        File file = new File(PATH + "/" + string);
        if (!file.exists()){
            if(file.createNewFile()) System.out.println("the file was created");
            else System.out.println("the file was not created for an unknown reason");
        } else {
            System.out.println("such file already exists");
        }
    }

    public void copyWithStream(String pathStart, String pathEnd) throws IOException {
        File file = new File(PATH + "/" + pathStart);
        if(!file.exists()){
            System.out.println("file does not exist");
            return;
        }
        file = new File(PATH + "/" +pathEnd);
        if(file.exists()){
            System.out.println("such file already exists");
            return;
        }
        try (InputStream is = new FileInputStream(pathStart); OutputStream os = new FileOutputStream(pathEnd)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }
}
