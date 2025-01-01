use std::io;

fn main() {

    let mut number: String = String::new();

    io::stdin()
        .read_line(&mut number)
        .expect("Failed to read number");

    let number: u32 = number.trim().parse()
        .expect("Not a valid number!");

    // println!("{number} fibonacci number is {}", fibonacci(number));

    for number in 1..number {
        println!("{number} fibonacci number is {}", fibonacci(number));
    }
}

// fn fibonacci(n: u32) -> u64 {
//     match n {
//         0 => 0,
//         1 => 1,
//         _ => fibonacci(n-1)+fibonacci(n-2)
//     }
// }

fn fibonacci(n: u32) -> u64 {
    let (current, _) = fibonacci_internal(n);
    current
}

fn fibonacci_internal(n: u32) -> (u64, u64) {
    match n {
        0 => (0, 0),
        1 => (1, 0),
        _ => { let (current, previous) = fibonacci_internal(n-1); (current+previous, current) }
    }
}

