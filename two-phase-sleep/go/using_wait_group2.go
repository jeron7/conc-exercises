package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
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
		succ_id := find_successor_id(i, n)
		var out chan int = channels[succ_id]
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
	sleep_time := generate_random_bounded_integer(6)
	fmt.Printf("Thread-%d will sleep for %d seconds.\n", id, sleep_time)
	time.Sleep(time.Duration(sleep_time) * time.Second)
	fmt.Printf("Thread-%d has wake up.\n", id)

	succ_sleep_time := generate_random_bounded_integer(11)
	out <- succ_sleep_time
	fmt.Printf("Thread-%d set %d seconds for his successor.\n", id, succ_sleep_time)
}

func generate_random_bounded_integer(bound int) int {
	random := rand.Intn(bound)
	return random
}

func second_phase(id int, in <-chan int) {
	sleep_time := <-in
	fmt.Printf("Thread %d will sleep, again. Now for %d seconds.\n", id, sleep_time)
	time.Sleep(time.Duration(sleep_time) * time.Second)
	fmt.Printf("Thread %d has wake up, again.\n", id)
}

func find_successor_id(id, n int) int {
	succ_id := id
	if id == n-1 {
		return 0
	}
	return succ_id + 1
}
