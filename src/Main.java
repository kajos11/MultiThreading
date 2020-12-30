import javax.management.RuntimeErrorException;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				//code that will run in new thread
				System.out.println("we are now im a new thread: "+Thread.currentThread().getName());
				System.out.println("Current thread priority is: "+Thread.currentThread().getPriority());
				throw new RuntimeException("Internal Exception");
			}
		});
		thread.setName("New Worker Thread");
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("a critical error happened in thread: "+t.getName()+
						" the error is: "+e.getMessage());
			}
		});
		System.out.println("we are executing: "+Thread.currentThread().getName()+" befire starting new thread");
		thread.start();
		System.out.println("we are executing: "+Thread.currentThread().getName()+" after starting new thread");
		Thread.sleep(10000);
	}

}

