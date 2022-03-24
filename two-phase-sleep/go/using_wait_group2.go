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
		pred_id := find_predecessor_id(i, n)
		var out chan int = channels[pred_id]
		var in chan int = channels[i]
		id := i
		go func() {
			first_phase(id, out)
			f_barrier.Done()
			f_barrier.Wait()
			second_phase(id, in)
			close(in)
			s_barrier.Done()
		}()
	}
	s_barrier.Wait()
}

func first_phase(id int, out chan<- int) {
	sleep_time := generate_random_bounded_integer()
	fmt.Printf("Thread-%d will sleep for %d seconds.\n", id, sleep_time)
	time.Sleep(time.Duration(sleep_time) * time.Second)
	fmt.Printf("Thread-%d has wake up.\n", id)

	pred_sleep_time := generate_random_bounded_integer()
	out <- pred_sleep_time
	fmt.Printf("Thread-%d set %d seconds for his predecessor.\n", id, pred_sleep_time)
}

func generate_random_bounded_integer() int {
	random := rand.Intn(random_upper_bound)
	return random
}

func second_phase(id int, in <-chan int) {
	sleep_time := <-in
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
