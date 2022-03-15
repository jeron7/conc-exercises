use std::sync::{Arc, Barrier, Mutex};
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
    let capacity = (n).try_into().unwrap();
    let first_barrier = Arc::new(Barrier::new(capacity));
    let second_barrier = Arc::new(Barrier::new((n + 1).try_into().unwrap()));
    let mut timers_vec : Vec<Duration> = Vec::with_capacity(capacity);
    for _ in 0..n {
        timers_vec.push(Duration::from_secs(0));
    }
    let timers = Arc::new(Mutex::new(timers_vec));
    for i in 0..n {
        let f_barrier_copy = Arc::clone(&first_barrier);
        let s_barrier_copy = Arc::clone(&second_barrier);
        let timers = Arc::clone(&timers);
        thread::spawn(move || {
            let mut rng = rand::thread_rng();
            let duration : Duration = Duration::from_secs(rng.gen_range(0..=5));
            println!("thread number {} will sleep for {} seconds", i, duration.as_secs());
            thread::sleep(duration);
            println!("thread number {} woke up", i);
            let duration_p : Duration = Duration::from_secs(rng.gen_range(0..=5));
            let mut index : usize = (n - 1).try_into().unwrap();
            if i > 0 {
                index = (i - 1).try_into().unwrap();
            }
            println!("thread {} defined {} seconds for thread {} sleep", i, duration_p.as_secs(), index);
            timers.lock().unwrap()[index] = duration_p;
            
            f_barrier_copy.wait();
            
            let i_duration : Duration = timers.lock().unwrap()[usize::try_from(i).unwrap()];
            println!("thread number {} will sleep, again, for {} seconds", i, i_duration.as_secs());
            thread::sleep(i_duration);
            println!("thread number {} woke up, again", i);
            s_barrier_copy.wait();
        });
    }

    second_barrier.wait();
}