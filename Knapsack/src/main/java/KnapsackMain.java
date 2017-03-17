import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class KnapsackMain {

	private static final Pattern p = Pattern.compile(",");

	public static void main(String[] args) throws IOException {
		Path path = Paths.get(args[1]);
		List<Baggage> outBaggages = new ArrayList<>();
		try (Stream<String> lines = Files.lines(path)) {
			Baggage[] baggages = lines.map(Baggage::toBaggage).sorted((a1, a2) -> a2.score.compareTo(a1.score))
//					.peek(System.out::println)
					.toArray(Baggage[]::new);
			BigDecimal knapsize = new BigDecimal(args[0]);
			BigDecimal currentsize = new BigDecimal(0);
			for (Baggage baggage : baggages) {
				if (knapsize.compareTo(currentsize.add(baggage.size)) > 0) {
					currentsize = currentsize.add(baggage.size);
					outBaggages.add(baggage);
				}
			}
		}

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(args[2]))) {
			outBaggages.stream()
			.peek(System.out::println)
			.map(Baggage::toOutputLine).forEach(t -> {
				try {
					writer.write(t);
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	static class Baggage {
		final private String id;
		final private BigDecimal size;
		final private BigDecimal priority;
		final private BigDecimal score;

		Baggage(String[] split) {
			this.id = split[0];
			this.size = new BigDecimal(split[1]);
			this.priority = new BigDecimal(split[2]);
			this.score = priority.divide(size, BigDecimal.ROUND_HALF_EVEN);
		}

		static Baggage toBaggage(String line) {
			return new Baggage(p.split(line));
		}

		public String toString() {
			return String.format("%s,%s,%s,%s", id, size, priority, score);
		}

		public String toOutputLine() {
			return String.format("%s,%s,%s", id, size, priority);
		}

		public BigDecimal getSize() {
			return this.size;
		}
	}
}
