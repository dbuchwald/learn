fn main() {
    let mut count = 0;
    'counting_up: loop {
        println!("count = {count}");
        let mut remaining = 10;

        loop {
            println!("remaining = {remaining}");
            if remaining == 9 {
                break;
            }
            if count == 2 {
                break 'counting_up;
            }
            remaining -= 1;
        }

        count += 1;
    }
    println!("End count = {count}");

    const COUNTDOWN_STARTING_VALUE: i32 = 10;

    for number in (1..COUNTDOWN_STARTING_VALUE).rev() {
        println!("{number}!");
    }

    println!("LIFTOFF!!!");

}