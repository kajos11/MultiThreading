
public class Interrupt {

	public static void main(String[] args) {
		
		Thread t = new Thread(new BlockingTask());
		t.setName("interrupted thread");
		t.start();
		t.interrupt();
	}
	
	private static class BlockingTask implements Runnable{

		@Override
		public void run() {
			try {
				Thread.sleep(500000);
			} catch (InterruptedException e) {
				System.out.println("exiting blocking thread");
			}
			
		}
		
	}

}
