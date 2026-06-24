import { motion, useScroll, useTransform } from "framer-motion";
import { useRef } from "react";

export default function Hero() {
  const containerRef = useRef(null);
  const { scrollYProgress } = useScroll({
    target: containerRef,
    offset: ["start start", "end start"]
  });

  // Parallax effects
  const y = useTransform(scrollYProgress, [0, 1], ["0%", "50%"]);
  const opacity = useTransform(scrollYProgress, [0, 0.8], [1, 0]);
  const scale = useTransform(scrollYProgress, [0, 1], [1, 0.9]);

  return (
    <section ref={containerRef} className="relative h-[110vh] flex items-center overflow-hidden bg-slate-50">
      {/* Background Cinematic Lighting */}
      <motion.div style={{ y, opacity, scale }} className="absolute inset-0 z-0 flex items-center justify-center">
        <div className="absolute w-[80vw] h-[80vw] md:w-[40vw] md:h-[40vw] rounded-full bg-primary-container/10 blur-[100px] top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2"></div>
      </motion.div>

      <div className="relative z-10 max-w-container-max mx-auto px-margin-mobile md:px-gutter w-full flex flex-col items-center text-center mt-20">
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1.2, ease: [0.16, 1, 0.3, 1], delay: 0.2 }}
        >
          <span className="text-primary-container font-bold tracking-widest text-xs uppercase mb-6 inline-block">
            Bengaluru's First Premium EV Network
          </span>
        </motion.div>

        <motion.h1 
          initial={{ opacity: 0, y: 60 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1.2, ease: [0.16, 1, 0.3, 1], delay: 0.4 }}
          className="font-display-lg-mobile md:font-display-lg text-[12vw] md:text-[6vw] leading-[1.1] tracking-tighter text-primary mb-8 max-w-5xl"
        >
          The Future of Bengaluru Mobility by <br />
          <span className="gradient-text italic pr-2 md:pr-4">Yaatra EV</span>
        </motion.h1>

        <motion.p 
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, ease: [0.16, 1, 0.3, 1], delay: 0.6 }}
          className="font-body-lg text-xl md:text-2xl text-text-secondary mb-12 max-w-2xl font-light"
        >
          Get exclusive early access to Yaatra EV and experience premium electric mobility before anyone else. Driving cleaner, traveling safer, and shaping a better tomorrow.
        </motion.p>

        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, ease: [0.16, 1, 0.3, 1], delay: 0.7 }}
          className="bg-surface-container-lowest/50 backdrop-blur-3xl border border-secondary/30 px-6 py-6 md:px-12 rounded-3xl mb-12 shadow-2xl relative overflow-hidden"
        >
          <div className="absolute inset-0 bg-gradient-to-r from-secondary/5 via-transparent to-secondary/5"></div>
          <div className="relative z-10 flex flex-col items-center">
            <span className="text-secondary font-bold text-sm uppercase tracking-widest mb-3 inline-block px-4 py-1 rounded-full bg-secondary/10">Limited Time Offer</span>
            <p className="text-primary text-xl md:text-2xl font-light leading-snug">
              Sign up now and receive <span className="font-bold text-secondary">₹500 in ride credits</span> + <br className="hidden md:block" />
              <span className="font-bold">Exclusive Founder Status</span> upon launch.
            </p>
          </div>
        </motion.div>

        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, ease: [0.16, 1, 0.3, 1], delay: 0.8 }}
          className="flex flex-col sm:flex-row gap-6"
        >
          <a href="#waitlist" className="px-10 py-5 bg-primary-container text-on-primary font-bold rounded-full electric-glow text-lg transition-transform hover:scale-105 active:scale-95 text-center">
            Join the Waitlist
          </a>
          <a href="#vision" className="px-10 py-5 bg-transparent border border-glass-border/40 hover:border-primary-container/40 text-primary font-bold rounded-full transition-all text-lg backdrop-blur-md text-center">
            Explore the Vision
          </a>
        </motion.div>
      </div>
      

    </section>
  );
}
