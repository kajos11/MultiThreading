import java.io.IOException;

public class WaitingForUserInputTest {

	public static void main(String [] args) {
		Thread thread = new Thread(new WaitingForUserInput());
		
		thread.setName("InputWaitingThread");
		thread.start();
	}
	
	private static class WaitingForUserInput implements Runnable {
		private String test;
		@Override
		public void run() {
			try {
				while (true) {
					char input = (char) System.in.read();
					if(input == 'q') {
						return;
					}
				}
			} catch (IOException e) {
				System.out.println("An exception was caught " + e);
			}
		}
	}
}