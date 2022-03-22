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
	fmt.Printf("The choosen number was: %d\n", n)
}

func start_threads_and_wait(n int) {
	barrier := &sync.WaitGroup{}
	barrier.Add(n)

	for i := 1; i <= n; i++ {
		go sleep_and_wake_up(i, barrier)
	}
	barrier.Wait()
}

func sleep_and_wake_up(id int, barrier *sync.WaitGroup) {
	sleep_for_seconds(id)
	barrier.Done()
}

func sleep_for_seconds(id int) {
	sleep_time := generate_random_bounded_integer(6)
	fmt.Printf("Thread (%d): will sleep for (%d) seconds\n", id, sleep_time)
	time.Sleep(time.Duration(sleep_time) * time.Second)
	fmt.Printf("Thread (%d): wake up\n", id)
}

func generate_random_bounded_integer(bound int) int {
	value := rand.Intn(bound)
	return value
}
