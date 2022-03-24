package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const (
	random_upper_bound int = 6
)

func main() {
	fmt.Printf("Choose a number: ")
	n := read_int()
	start_threads_and_wait(n)
	fmt.Printf("\nThe choosen number was: %d\n", n)
}

func read_int() int {
	var value int
	fmt.Scanf("%d", &value)
	return value
}

func start_threads_and_wait(n int) {
	channels := make([]chan int, n)
	for i := 0; i < n; i++ {
		channels[i] = make(chan int, 1)
	}
	f_barrier := &sync.WaitGroup{}
	f_barrier.Add(n)
	s_barrier := &sync.WaitGroup{}
	s_barrier.Add(n)

	for i := 0; i < n; i++ {
		go sleep_and_wake_up(i, f_barrier, s_barrier, channels)
	}
	s_barrier.Wait()
}

func sleep_and_wake_up(id int, f_barrier, s_barrier *sync.WaitGroup, channels []chan int) {
	first_phase(id, channels)
	f_barrier.Done()
	f_barrier.Wait()
	second_phase(id, channels)
	s_barrier.Done()
}

func first_phase(id int, channels []chan int) {
	sleep_time := generate_random_bounded_integer()
	fmt.Printf("Thread-%d will sleep for %d seconds.\n", id, sleep_time)
	time.Sleep(time.Duration(sleep_time) * time.Second)
	fmt.Printf("Thread-%d has wake up.\n", id)

	pred_id := find_predecessor_id(id, len(channels))
	pred_sleep_time := generate_random_bounded_integer()
	channels[pred_id] <- pred_sleep_time
	fmt.Printf("Thread-%d set %d seconds for thread-%d.\n", id, pred_sleep_time, pred_id)
}

func generate_random_bounded_integer() int {
	random := rand.Intn(random_upper_bound)
	return random
}

func second_phase(id int, channels []chan int) {
	sleep_time := <-channels[id]
	close(channels[id])
	fmt.Printf("Thread %d will sleep, again. Now for %d seconds.\n", id, sleep_time)
	time.Sleep(time.Duration(sleep_time) * time.Second)
	fmt.Printf("Thread %d has wake up, again.\n", id)
}

func find_predecessor_id(id, n int) int {
	pred_id := id
	if id == 0 {
		pred_id = n
	}
	return pred_id - 1
}
