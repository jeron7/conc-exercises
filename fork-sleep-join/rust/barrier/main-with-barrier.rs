use std::sync::{Arc, Barrier};
use std::{io::stdin, thread, time::Duration};
use rand::Rng;

fn main() {
    println!("Type a number n below");

    let n: i32 = input_number();

    create_threads_and_wait(n);

    println!("The number that you've been wait for: {}", n);
}

fn input_number() -> i32 {
    let mut input_line = String::new();
    stdin()
        .read_line(&mut input_line)
        .expect("Failed to read line");
    let n: i32 = input_line
        .trim().parse()
        .expect("Input is not an integer");
    return n;
}

fn create_threads_and_wait(n: i32) {
    let size = (n + 1).try_into().unwrap();
    let barrier = Arc::new(Barrier::new(size));
    for i in 0..n {
        let c = Arc::clone(&barrier);
        thread::spawn(move || {
            let mut rng = rand::thread_rng();
            let duration : Duration = Duration::from_secs(rng.gen_range(0..=5));
            println!("thread number {} will sleep for {} seconds", n - i, duration.as_secs());
            thread::sleep(duration);
            println!("thread number {} woke up", n - i);
            c.wait();
        });
    }

    barrier.wait();
}