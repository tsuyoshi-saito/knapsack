import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ナップサック問題 {

	private static final Pattern p = Pattern.compile(",");

	public static void main(String[] args) throws IOException {
		String args0 = "20";
		String args1 = "C:\\workspaces\\devs\\Knapsack\\input_file.csv";
		String args2 = "C:\\workspaces\\devs\\Knapsack\\output2_file.csv";
		Path path = Paths.get(args1);
		List<String> ids = new ArrayList<>();
		try (Stream<String> lines = Files.lines(path)) {
			Baggage[] array = lines.map(Baggage::toBaggage).sorted((a1, a2) -> Double.compare(a2.score, a1.score)).toArray(Baggage[]::new);
			int knapsize = Integer.parseInt(args0);
			double currentsize = 0;
			for (Baggage a : array) {
				currentsize += a.size;
				if (currentsize > knapsize) {
					break;
				}
				ids.add(a.id);
				System.out.println(a.toString());
				System.out.println(currentsize);
			}
		}
	}

	static class Baggage {
		final public String id;
		final public double size;
		final public double priority;
		final public double score;

		Baggage(String[] split) {
			this.id = split[0];
			this.size = Double.parseDouble(split[1]);
			this.priority = Double.parseDouble(split[2]);
			this.score = priority / size;
		}

		static Baggage toBaggage(String line) {
			return new Baggage(p.split(line));
		}

		public String toString() {
			return String.format("%s,%s,%s,%s", id, size, priority, score);
		}
	}
}
