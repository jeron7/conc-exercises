package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

func main() {
	fmt.Printf("Choose a number: ")
	var n int
	fmt.Scanf("%d", &n)
	start_threads_and_wait(n)
	fmt.Printf("\nThe choosen number was: %d\n", n)
}

func start_threads_and_wait(n int) {
	f_barrier := &sync.WaitGroup{}
	f_barrier.Add(n)
	s_barrier := &sync.WaitGroup{}
	s_barrier.Add(n)
	ch := make([]chan int, n)
	for i := 0; i < n; i++ {
		chn := make(chan int, 1)
		ch[i] = chn
	}
	for i := 0; i < n; i++ {
		go sleep_and_wake_up(i, f_barrier, s_barrier, ch)
	}
	s_barrier.Wait()
}

func sleep_and_wake_up(id int, f_barrier, s_barrier *sync.WaitGroup, ch []chan int) {
	first_phase(id, ch)
	f_barrier.Done()
	f_barrier.Wait()
	second_phase(id, ch)
	s_barrier.Done()
}

func first_phase(id int, ch []chan int) {
	sleep_time := generate_random_bounded_integer(6)
	fmt.Printf("Thread-%d will sleep for %d seconds.\n", id, sleep_time)
	time.Sleep(time.Duration(sleep_time) * time.Second)
	fmt.Printf("Thread-%d has wake up.\n", id)

	pred_index := find_predecessor_index(id, len(ch))
	random_int := generate_random_bounded_integer(6)
	ch[pred_index] <- random_int
	fmt.Printf("Thread-%d set %d seconds for thread-%d.\n", id, random_int, pred_index)
}

func generate_random_bounded_integer(bound int) int {
	random := rand.Intn(bound)
	return random
}

func second_phase(id int, ch []chan int) {
	sleep_time := <-ch[id]
	fmt.Printf("Thread %d will sleep, again. Now for (%d) seconds.\n", id, sleep_time)
	time.Sleep(time.Duration(sleep_time) * time.Second)
	fmt.Printf("Thread %d has wake up, again.\n", id)
}

func find_predecessor_index(current, n int) int {
	index := current
	if current == 0 {
		index = n
	}
	return index - 1
}
