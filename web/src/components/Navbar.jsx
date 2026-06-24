import { motion } from "framer-motion";

export default function Navbar() {
  return (
    <motion.nav 
      initial={{ y: -100, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ duration: 1, ease: [0.16, 1, 0.3, 1] }}
      className="fixed top-0 w-full z-50 bg-background/50 backdrop-blur-2xl border-b border-glass-border/40"
    >
      <div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter py-4 flex justify-between items-center">
        <motion.div 
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ delay: 0.5, duration: 0.8 }}
          className="font-headline-md text-headline-md font-bold text-primary tracking-tighter"
        >
          VoltRide
        </motion.div>
        
        <div className="hidden md:flex items-center gap-stack-lg">
          {[
            { label: "Models", href: "#models" },
            { label: "Why VoltRide", href: "#vision" },
            { label: "Impact", href: "#impact" }
          ].map((item, i) => (
            <motion.a 
              key={item.label}
              href={item.href}
              initial={{ opacity: 0, y: -10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.5 + i * 0.1, duration: 0.6 }}
              className="font-body-md text-on-surface-variant hover:text-primary transition-colors duration-300 cursor-pointer"
            >
              {item.label}
            </motion.a>
          ))}
          <motion.a 
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.8, duration: 0.6 }}
            className="font-body-md text-primary hover:text-primary-container transition-colors duration-300 cursor-pointer"
            href="#waitlist"
          >
            Waitlist
          </motion.a>
        </div>

        <motion.a 
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ delay: 0.9, duration: 0.6 }}
          href="#waitlist"
          className="px-6 py-2 bg-primary-container text-on-primary font-bold rounded-full electric-glow transition-transform active:scale-95"
        >
          Join Waitlist
        </motion.a>
      </div>
    </motion.nav>
  );
}
