import java.util.*;

public class FillerTest {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>(10);
		for(int i = 0; i < 10; i++)
      list.add("");
		String filler = args[0];
		Collections.fill(list, filler);
		for (String s : list)
			System.out.println(s);
	}
}
